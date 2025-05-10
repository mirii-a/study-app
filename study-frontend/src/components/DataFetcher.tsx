
async function fetchDataFromApi() => {
    let response = await fetch('http://localhost:9193/api/v1/words/all');
    if (!response.ok) {
        throw new Error("Unable to fetch all word data...");
    }
    return await response.text
}