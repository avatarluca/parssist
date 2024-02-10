export function saveInLocalStorage(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

export function getInLocalStorage(key) {
    if(localStorage.getItem(key) == null) return null;
    else {
        return JSON.parse(localStorage.getItem(key));
    }
}