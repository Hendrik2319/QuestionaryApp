
export function generateRandomString( length: number): string
{
    const arr = Array(length);
    arr.fill('');
    return arr
        .map(() => String.fromCharCode( Math.floor(Math.random()*26) + "A".charCodeAt(0) ))
        .join("");
}
