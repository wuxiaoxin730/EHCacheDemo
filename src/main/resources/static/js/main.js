var clearUp = function() {
    return confirm("Are you sure you want to clear all the data under current cache?");
}

var removeElement = function() {
    return confirm("Do you want to delete the current record?");
}

var addElement = function(key) {
    if(key === undefined || key === null || key.trim() === ""){
        alert("Please input [Cache Key]!");
        return false;
    }
    return true;
}