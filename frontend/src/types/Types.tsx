
export type PageDirection = "PREV" | "NEXT";

export type PolymorphicValue =
| {
    type: "BOOL",
    value: boolean,
}
| {
    type: "STRING",
    value: string,
}
