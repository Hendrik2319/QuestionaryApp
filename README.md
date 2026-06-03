# QuestionaryApp
Mit dieser kleine Web-Anwendung wird dem Benutzer ein zuvor definierter Fragebogen angezeigt.   
Es werden die Fragen einzeln präsentiert. Man kann dabei im Fragebogen vor- und zurückgehen. Fragen können auch vollständig unbeantwortet bleiben.
Am Ende wird eine Zusammenfassung angezeigt, in der die Eingaben auch als
<a href="#resume_file">Datei</a> herunterladen werden können.   
Wenn die Anwendung / der Server frisch gestartet ist, wird der erste Besucher dazu aufgefordert,
eine <a href="#questionary_definition">Fragebogen-Definition</a> hochzuladen. War dies erfolgreich,
wird er, wie alle späteren Besucher, zur 1. Frage weiter geleitet.



## Verwendete Dateien
Im Folgenden möchte ich hier das Format der beiden verwendeten Dateien (
<a href="#questionary_definition">Fragebogen-Definition</a>
und
<a href="#resume_file">Zusammenfassung der Eingaben</a>
) zeigen.



<a id="questionary_definition"></a>
### Fragebogen-Definition

Die Dateien für die Definition eines Fragebogens sind JSON-Dateien,
die beim Upload über das Frontend direkt zum Backend weitergereicht und erst dort geparst werden.
Da ich es für übersichtlicher halte, würde ich an dieser Stelle dennoch gern eine `TypeScript`-Type-Definition
zeigen, die den Inhalt einer solche Definitionsdatei beschreibt (beginnend mit dem äußersten Objekt):

    type QuestionaryDef = {
    	title: string                   // Titel des Fragebogens
    	questions: QuestionDef[]        // einzelne Definitionen der Fragen
	}

	type QuestionDef =
		| BoolQuestionDef
		| ChoiceQuestionDef
		| QuestionGroupDef

    interface QuestionDef_Base {
		id: string                      // vom Ersteller der Datei festgelegte ID der Frage (Anmerkungen: siehe unten)
		text: string                    // der Text der eigentlichen Frage
		conditions: ConditionsGroupDef  // Definition von Bedingungen für diese Frage (Anmerkungen: siehe unten)
    }
	
    interface BoolQuestionDef extends QuestionDef_Base {
    	type: "BOOL",                   // - keine weiteren Felder nötig -
    }

    interface ChoiceQuestionDef extends QuestionDef_Base {
    	type: "SINGLE" | "MULTIPLE",    // soll nur eine Option wählbar oder Mehrfach-Auswahl möglich sein
	    options: OptionDef[],           // welche Optionen stehen zur Auswahl
    }

    interface QuestionGroupDef extends QuestionDef_Base {
    	type: "GROUP",                  // eine Frage, die aus mehreren Teil-Fragen besteht, darf jedoch keine weitere QuestionGroup enthalten
    	sub_questions: QuestionDef[],   // die Teilfragen
    }


	type OptionDef = {
		value: string                   // der interne Wert dieser Option
		label?: string                  // Beschriftung dieser Option
		                                //    kann auch entfallen -> dann wird der Text von 'value' als Beschriftung verwendet
	}


	type ConditionsGroupDef = {
		type: "ALL" | "ONE"             // müssen alle Teilbedingungen erfüllt sein oder reicht eine
		conditions: ConditionDef[]      // Liste der Teilbedingungen
	}
	type ConditionDef = {
		question_id: string             // ID der Frage, auf die sich die Bedingung bezieht (Anmerkungen: siehe unten)
		value: PolymorphicValue         // Wert, mit dem diese Frage (-> question_id ) beantwortet worden sein muss, damit die Bedingung als erfüllt gilt
	}


	type PolymorphicValue =
		|	{
				type: "BOOL",           // eine Antwort einer BoolQuestion
				value: boolean,
			}
		|	{
				type: "STRING",         // eine Antwort einer ChoiceQuestion
				value: string,          // entspricht dem, was in OptionDef als 'value' festgelegt wurde (Anmerkungen: siehe unten)
			}

#### ID einer Frage
`QuestionDef_Base.id`. `ConditionDef.question_id`   
Diese ID wird zur Identifikation der Frage innerhalb des Dokuments verwendet. Sie kann frei gewählt werden.
Wenn die Datei als UTF-8 kodiert gespeichert wird, sind hier sogar Sonderzeichen möglich.
Die ID sollte auch beschreibend und wiedererkennbar sein, damit man beim Erstellen der Definition bzw.
beim nachträglichen Lesen (durch einen Menschen) leicht erkennt auf welche Frage sich bezogen wird.

<a id="question_conditions"></a>
#### Bedingungen einer Frage
`QuestionDef_Base.conditions`, `ConditionsGroupDef`. `ConditionDef`   
Zu jeder Frage können mehrere Bedingungen definiert werden, die steuern, wann es überhaupt sinnvoll ist,
die jeweilige Frage anzuzeigen. Dabei wird in jeder Bedingung mittels `question_id` festgelegt,
auf welche andere Frage sich die Bedingung bezieht. Damit ist die Frage gemeint, die mit einer bestimmten
Antwort (`ConditionDef.value`) beantwortet worden sein muss, damit die aktuelle Bedingung als erfüllt gilt.   
Es darf sich hierbei immer nur auf eine der vorhergehenden Fragen bezogen werden.
Ein Bezug auf eine Fragen-Gruppe (`QuestionGroupDef`) ist nicht zulässig, weil eine solche Gruppe
als Ganzes keine Antworten haben kann, sondern nur ihre Teilfragen. Auf die kann ich dann wieder bezogen werden.   
In `ConditionDef.value` wird der gesuchte Wert als `string` oder als `boolean` abgelegt,
natürlich gekapselt als Objekt und mit `type` zu Unterscheidung.

#### Optionen von Auswahl-Fragen
`ChoiceQuestionDef.options`, `OptionDef`   
Für jede dieser Optionen muss zumindest der Wert `value` definiert werden.
Die Beschriftung (`label`) der Option kann entfallen. Es wird in diesem Fall der Text von `value` als Beschriftung verwendet.
Der Wert von `value` findet sich am Ende in der <a href="#resume_file">Zusammenfassung</a> wieder.
Darüber sollen bei etwaiger Weiterverarbeitung der Zusammenfassung die gegebenen Antworten identifiziert werden können.
`value` muss dafür nur innerhalb der Frage eindeutig sein. Andere Fragen können dieselben Werte wiederverwenden.

#### Verbotenes
Neben den schon genannten Punkten sind auch folgende Dinge in der Definition nicht erlaubt:
* eine leere Liste (`QuestionGroupDef.sub_questions`) von Teilfragen in einer Fragengruppe (`QuestionGroupDef`)
* ein Fragebogen-Definition ohne eine einzige Frage (-> `QuestionaryDef.questions`)
* die erste Frage im Fragebogen darf selbst keine Bedingungen haben (Es gäbe auch keine Frage, auf die sich diese Bedingungen beziehen könnten.)
* Die ID (`QuestionDef_Base.id`) einer Frage darf nicht leer sein oder nur aus Leerzeichen bestehen.
Technisch wäre es machbar, solange die ID eindeutig wäre. Es widerspräche aber der Idee, dass die IDs beschreibend sein sollen.
* ein Bezug (`ConditionDef.question_id`) auf eine nicht vorhandene ID
* eine Frage ohne Text (`QuestionDef_Base.text`) oder nur aus Leerzeichen
* ein Fragebogen ohne oder mit leerem Titel (`QuestionaryDef.title`)
* eine Auswahl-Frage ohne Optionen (`ChoiceQuestionDef.options`)
* eine Option einer Auswahl-Frage ohne oder mit leerem `value`-Feld ("leer" meint "aus Leerzeichen bestehend")
* nicht eindeutige Optionen: mehrere Optionen einer Frage haben denselben Wert in `value`
* eine Fragen-Gruppe ohne Teilfragen



<a id="resume_file"></a>
### Zusammenfassung der Eingaben

Die Zusammenfassung ist ebenfalls eine JSON-Datei.
Um es möglichst überschaubar darzustellen, zeige
ich auch hier die Type-Definition aus TypeScript:

    interface Answer_Base {
    	questionId: string                        // ID der Frage, von der die Antwort(en) stamm(t/en)
    }

    interface BoolAnswer extends Answer_Base {
    	type: "BOOL"                              // Antwort einer BoolQuestion
    	answer: boolean
    }

    interface SingleChoiceAnswer extends Answer_Base {
    	type: "SINGLE"                            // Antwort einer Einzel-Auswahl
    	answer: string                            // entspricht Wert aus OptionDef.value aus Definition
    }
	
    interface MultipleChoiceAnswer extends Answer_Base {
    	type: "MULTIPLE"                          // Antworten einer Mehrfach-Auswahl
    	answers: string[]                         // enthält Werte aus mehreren OptionDef.value aus Definition
    }
    
    type Answer =                                 // eine Antwort kann einem der 3 obigen Typen entsprechen
    	| BoolAnswer                              //     'Answer' entspricht der Antwort / den Antworten einer Frage
    	| SingleChoiceAnswer
    	| MultipleChoiceAnswer
    
    type Download = {
    	sessionId: string                         // Session ID, anhand der der Anwender während der Befragung identifiziert wird
    	answers: Answer[]                         // List der Antworten; zu jeder beantworteten Frage ein Eintrag
    }

In der Anwendung werden die Antworten in ein solches `Download` Objekt zusammengefasst,
in JSON umgewandelt und als Text-Datei dem Anwender per Link zum Download angeboten.

Es werden dabei aber nur die Antworten von Fragen berücksichtigt,
die aufgrund der Antworten der vorherigen Fragen noch als relevant gelten.
Unberücksichtigt bleiben z.B. Fragen, die sich auf einen Sachverhalt beziehen,
der in einer vorherigen Frage schon verneint wurde
(siehe "<a href="#question_conditions">Bedingungen einer Frage</a>": `QuestionDef_Base.conditions` bzw. `ConditionsGroupDef` aus <a href="#questionary_definition">Fragebogen-Definition</a>).
Es werden natürlich auch Fragen übersprungen, zu denen gar keine Anworten abgegeben wurden.






## Änderungen für später
* introduce internal question IDs
	* in 1:1 assignment to given question IDs
	* to use as path variable in requests
