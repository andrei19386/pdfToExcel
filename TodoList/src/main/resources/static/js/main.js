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
        case 'task-box': changeCompletedStatus(target);
     }
     
}

function editItem(target) {
    let newValue = prompt("Введите новое значение:");
    if(newValue!='') {
    let xhttp = new XMLHttpRequest();

         xhttp.onreadystatechange = function(){
                           if(this.readyState == 4 && this.status == 200){
                                   target.parentElement.outerHTML = generateHTML(target.name, newValue);
                                   storageMap.set(target.name, newValue);
                           }  else if(this.readyState == 4) alert("Error in editing!")
                       }
         console.log(`http://127.0.0.1:8080/case/${target.name}`);
         xhttp.open('PUT',`http://127.0.0.1:8080/case/${target.name}`,true);
          let formData = new FormData(document.forms.case_);
          formData.append("name", newValue);
          xhttp.send(formData);
    }
}

function deleteItem(target) {
     let xhttp = new XMLHttpRequest();

     xhttp.onreadystatechange = function(){
                       if(this.readyState == 4 && this.status == 200){
                           let id = postData(this.responseText);
                           target.parentElement.remove();
                           storageMap.delete(target.name);
                               document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
                               if (storageMap.size + completedMap.size == 0) {
                                   document.querySelector('.no-tasks-message').style.display = "flex";
                               }
                       }  else if(this.readyState == 4) alert("Error in deleting!")
                   }
     console.log(`http://127.0.0.1:8080/case/${target.name}`);
     xhttp.open('DELETE',`http://127.0.0.1:8080/case/${target.name}`,true);
     xhttp.send();
}

function changeCompletedStatus(target){
    console.log(target.firstElementChild.name);
    console.log(storageMap);
    if(storageMap.has(+target.firstElementChild.name)){

            let xhttp = new XMLHttpRequest();

                     xhttp.onreadystatechange = function(){
                                       if(this.readyState == 4 && this.status == 200){
                                              let value = storageMap.get(+target.firstElementChild.name);
                                              target.style.textDecoration = 'line-through';
                                              target.children[0].style.display='none';
                                              target.children[1].style.display='none';
                                              storageMap.delete(+target.firstElementChild.name);
                                              console.log(storageMap);
                                              completedMap.set(+target.firstElementChild.name,value);
                                              console.log(completedMap);
                                              document.querySelector('.tasks-completed').firstElementChild.innerHTML = completedMap.size;
                                              document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
                                       }  else if(this.readyState == 4) alert("Error in changeStatus!")
                                   }
                     console.log(`http://127.0.0.1:8080/case/completed/${target.firstElementChild.name}`);
                     xhttp.open('PUT',`http://127.0.0.1:8080/case/completed/${target.firstElementChild.name}`,true);
                     xhttp.send();



    } else {
     let xhttp = new XMLHttpRequest();

             xhttp.onreadystatechange = function(){
                 if(this.readyState == 4 && this.status == 200){

                    let value = completedMap.get(+target.firstElementChild.name);
                    target.style.textDecoration = 'none';
                    target.children[0].style.display='flex';
                    target.children[1].style.display='flex';
                    completedMap.delete(+target.firstElementChild.name);
                    storageMap.set(+target.firstElementChild.name,value)
                    document.querySelector('.tasks-completed').firstElementChild.innerHTML = completedMap.size;
                        document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
            }  else if(this.readyState == 4) alert("Error in changeStatus!")
            }
            console.log(`http://127.0.0.1:8080/case/completed/${target.firstElementChild.name}`);
                                 xhttp.open('PUT',`http://127.0.0.1:8080/case/completed/${target.firstElementChild.name}`,true);
                                 xhttp.send();
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

          let xhttp = new XMLHttpRequest();

               xhttp.onreadystatechange = function(){
                   if(this.readyState == 4 && this.status == 200){
                       let id = postData(this.responseText);
                       document.querySelector('.tasks-content').innerHTML += generateHTML(id,taskText);
                       storageMap.set(`${id}`,taskText);
                       document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
                       document.querySelector('.add-task').firstElementChild.value="";
                       document.querySelector('.no-tasks-message').style.display = "none";
                   } else if(this.readyState == 4) alert("Error in addding!")
               }
               let formData = new FormData(document.forms.case_);
               formData.append("name", taskText);

               xhttp.open('POST','http://127.0.0.1:8080/case/',true);
               xhttp.send(formData);

    } else {
        alert("Поле ввода не может быть пустым!")
    }
}

function getData(data){
    let result = JSON.parse(data);
    console.log(data);
    console.log(result);
    for(let i=0; i < result.length;i++){
            let item = result[i];
            if(item.completed == false) {
                 storageMap.set(item.id,item.name)
            } else {
                completedMap.set(item.id,item.name)
            }
        }
}


function postData(data){
    return data;
}


function showTasks(){
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                getData(this.responseText);
                clearTaskBoxes();
                    if(storageMap.size + completedMap.size == 0){
                        document.querySelector('.no-tasks-message').style.display = "flex";
                    } else {
                        document.querySelector('.no-tasks-message').style.display = "none";
                        formHTMLTaskBox(storageMap);
                    }

                    document.querySelector('.tasks-completed').firstElementChild.innerHTML = completedMap.size;
                    document.querySelector('.tasks-count').firstElementChild.innerHTML = storageMap.size;
            }  else if(this.readyState == 4) alert("Error in getting information!")
        }

        xhttp.open('GET','http://127.0.0.1:8080/case/',true);
        xhttp.send();

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
