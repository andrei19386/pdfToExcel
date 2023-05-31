let storageMap = new Map();//Имитирует выделенную память под поставленные, но не завершенные задачи
let completedMap = new Map();//Имитирует выделенную память под завершенные задачи

showTasks();

document.querySelector('.tasks-content').onclick = function(event){
     let target = event.target;
     switch(target.className){
        case 'delete': deleteItem(target); 
        break;
        case 'edit': editItem(target);
        break;
        case 'task-box': changeCompletedStatus(target,storageMap);
     }
     
}

function editItem(target) {
    let newValue = prompt("Введите новое значение:");
    localStorage.setItem(target.name, JSON.stringify([newValue, 0])); //put-запрос
    target.parentElement.outerHTML = generateHTML(target.name, newValue);
    storageMap.set(target.name, newValue);
}

function deleteItem(target) {
    localStorage.removeItem(target.name); //delete-запрос
    target.parentElement.remove();
    storageMap.delete(target.name);
    document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
    if (storageMap.size + completedMap.size == 0) {
        document.querySelector('.no-tasks-message').style.display = "flex";
    }
}

function changeCompletedStatus(target,storageMap){
    if(storageMap.has(target.firstElementChild.name)){
            let value = storageMap.get(target.firstElementChild.name);
            target.style.textDecoration = 'line-through';
            target.children[0].style.display='none';
            target.children[1].style.display='none';
            storageMap.delete(target.firstElementChild.name);
            completedMap.set(target.firstElementChild.name,value)         
            localStorage.setItem(target.firstElementChild.name,JSON.stringify([value,1]));
            document.querySelector('.tasks-completed').firstElementChild.innerHTML = completedMap.size;
            document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size; 
    } else {
            let value = completedMap.get(target.firstElementChild.name);
            target.style.textDecoration = 'none';
            target.children[0].style.display='flex';
            target.children[1].style.display='flex';
            completedMap.delete(target.firstElementChild.name);
            storageMap.set(target.firstElementChild.name,value)         
            localStorage.setItem(target.firstElementChild.name,JSON.stringify([value,0]));
            document.querySelector('.tasks-completed').firstElementChild.innerHTML = completedMap.size;
            document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size; 
    }
}


function generateHTML(key,value){//Повторяющийся фрагмент генерации HTML-кода при добавлении, редактировании и отображении todolist
    return `<span class="task-box" id=task_${key}>
    ${value}
    <button class="delete" name=${key}>delete</button><button class="edit" name=${key}>edit</button></span>`;
}



document.querySelector('.plus').onclick = () => {
    let taskText = document.querySelector('.add-task').firstElementChild.value;
    if(taskText != ''){
        let currentKey = +getMaxKey(storageMap) + 1;
        localStorage.setItem(currentKey,JSON.stringify([taskText,0]));   //post-запрос
        document.querySelector('.no-tasks-message').style.display = "none";


        document.querySelector('.tasks-content').innerHTML += generateHTML(currentKey,taskText);
            storageMap.set(`${currentKey}`,taskText);
            document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
            document.querySelector('.add-task').firstElementChild.value="";
    } else {
        alert("Поле ввода не может быть пустым!")
    }
}

function getData(data){
    console.log(data)
}


function showTasks(){
        let xhttp = new XMLHttpRequest();
        xhttp.withCredentials = true;

        xhttp.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                getData(this.responseText);
            }
        }

        xhttp.open('GET','http://127.0.0.1:8080/',true);

        xhttp.send();

     
     storageMap = getMapFromLocalStorage('0');//get-запросы
     completedMap = getMapFromLocalStorage('1');

    clearTaskBoxes();
    if(storageMap.size + completedMap.size == 0){
        document.querySelector('.no-tasks-message').style.display = "flex";
    } else {
        document.querySelector('.no-tasks-message').style.display = "none";
        formHTMLTaskBox(storageMap);
    }
    
    document.querySelector('.tasks-completed').firstElementChild.innerHTML = completedMap.size;
    document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size; 

}

//Очистка фрагментов, чтобы при перезагрузке страницы не было добавления дублирующих записей
function clearTaskBoxes() {
    let elements = document.querySelectorAll('.task-box');
    for (let element of elements) {
        element.remove();
    }
}

//Формирует tasks-content
function formHTMLTaskBox(storageMap) {
    for (let entry of storageMap) {
        document.querySelector('.tasks-content').innerHTML += generateHTML(entry[0],entry[1]);
    }
    for (let entry of completedMap) {
        document.querySelector('.tasks-content').innerHTML += generateHTML(entry[0],entry[1]);
        let target = document.querySelector(`#task_${entry[0]}`);
        target.style.textDecoration = 'line-through';
        target.children[0].style.display='none';
        target.children[1].style.display='none';
    }
}

//Определяет максимальный ключ в Map. Это нужно, чтобы избежать повторения ключей 
//при добавлении новых элементов (новый добавляется увеличением максимального занчения ключа на 1)
function getMaxKey(storageMap){
    let max = 0;
    for(let entry of storageMap){
        if(entry[0]>max){
            max = entry[0];
        }
    }
    return max;
}

//Загружает из localStorage данные в списки задач storageMap и completedMap
//id - идентификатор статуса задачи в localStorage: 0 - не выполнена, 1 - выполнена
function getMapFromLocalStorage(id){
    let map = new Map();
    for(let i=0; i < localStorage.length;i++){
        let item = JSON.parse(localStorage.getItem(localStorage.key(i)));
        if(item[1]==id){
            map.set(localStorage.key(i),item[0]);
        }
    }
    return map;
}

