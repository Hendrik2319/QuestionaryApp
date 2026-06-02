import type { OptionDef } from "./DefinitionsTypes";

interface Page_Base {
    id: string,
    texts: string[],
    is_first: boolean,
}

interface BoolPage extends Page_Base {
    type: "BOOL",
}

interface ChoicePage extends Page_Base {
    type: "SINGLE" | "MULTIPLE",
    options: OptionDef[],
}

export type Page = BoolPage | ChoicePage;
