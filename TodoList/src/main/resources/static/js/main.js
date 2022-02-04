const put_ = function(id){
                var data = $('#case-edit-form form').serialize();
                $('#case-edit-form').css('display', 'none');
                $.ajax({
                            method: "PUT",
                            url: '/cases/' + id,
                            data: data,
                            success: function(response)
                            {
                                location.reload();
                            },
                            error: function(response)
                                        {
                                            if(response.status == 404) {
                                                alert('Дело не найдено!');
                                            }
                                        }
                        });
            };


const delete_ = function(id){
        $.ajax({
                                   method: "DELETE",
                                   url: '/cases/' + id,
                                   success: function(response)
                                   {
                                       location.reload()
                                    },
                                    error: function(response)
                                                {
                                                    if(response.status == 404) {
                                                        alert('Дело не найдено!');
                                                    }
                                                }

                               });
    }


 const edit_ = function(id) {
            $.ajax({
                        method: "GET",
                        url: '/cases/' + id,
                        success: function(response)
                        {
                             var code = '<input type="text" name="name" value="' + response.name + '">' +
                            '<button id="edit-case" onclick = "put_(' + id + ')">Сохранить</button>';
                             $('#case-edit-form form').append(code);
                        },
                        error: function(response)
                        {
                            if(response.status == 404) {
                                alert('Дело не найдено!');
                            }
                        }
                    });


            $('#case-edit-form').css('display', 'flex');

     }





$(function(){


    const appendCase = function(data){
        var caseCode = '<a href="#" class="case-link" data-id="' +
            data.id + '">' + data.name + '</a><br>' +
      '<button id="edit_' + data.id + '" class = editButton onclick = "edit_(' + data.id +
      ')">Редактировать</button>' +
     ' <button id="delete_' + data.id + '" class = deleteButton onclick = "delete_(' + data.id +
      ')">Удалить</button>';

        $('#toDo-list')
            .append('<div>' + caseCode + '</div>');
    };

    //Loading Cases on load page
    $.get('/cases/', function(response)
    {
     $('#case-form').css('display', 'none');
     $('#case-edit-form').css('display', 'none');
        for(i in response) {
             appendCase(response[i]);
        }
    });

    //Show adding case form
    $('#show-add-case-form').click(function(){
        $('#case-form').css('display', 'flex');
    });



    //Closing adding case form
    $('#case-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Closing edit case form
        $('#case-edit-form').click(function(event){
            if(event.target === this) {
                $(this).css('display', 'none');
            }
        });

    //Getting case
    $(document).on('click', '.case-link', function(){
        var link = $(this);
        var caseId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/cases/' + caseId,
            success: function(response)
            {
                var date = new Date(response.date);
                var month = date.getMonth() + 1;
                var year = date.getFullYear();
                var day = date.getDate();
                if (month < 10) {
                    if(day < 10){
                        var code = '<span> Дата добавления: 0' + day + '.0' + month + '.' + year + '</span>';
                    } else {
                        var code = '<span> Дата добавления: ' + day + '.0' + month + '.' + year + '</span>';
                    }

                } else {
                    if(day < 10){
                         var code = '<span> Дата добавления: 0' + day + '.' + month + '.' + year + '</span>';
                     } else {
                         var code = '<span> Дата добавления: ' + day + '.' + month + '.' + year + '</span>';
                     }
                }
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status == 404) {
                    alert('Дело не найдено!');
                }
            }
        });
        return false;
    });

    //Adding case
    $('#save-case').click(function()
    {
        var data = $('#case-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/cases/',
            data: data,
            success: function(response)
            {
                $('#case-form').css('display', 'none');
                var case_ = {};
                case_.id = response;
                var dataArray = $('#case-form form').serializeArray();
                for(i in dataArray) {
                    case_[dataArray[i]['name']] = dataArray[i]['value'];
                }
                appendCase(case_);
            }
        });
        return false;
    });

    //deleting All cases
    $('#delete_All').click(function() {
    $.ajax({
                method: "DELETE",
                url: '/cases/',
                success: function(response)
                {
                    location.reload()
                 }
            });
            return false;
    });

});