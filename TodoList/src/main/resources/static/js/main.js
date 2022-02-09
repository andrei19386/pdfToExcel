const put_ = function(id){
                var data = $('#case-edit-form form').serialize();
                 $('#case-edit-form').css('display', 'none');
                $.ajax({
                            method: 'PUT',
                            url: '/cases/' + id,
                            async: false,
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
                                        },
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
            async: false,
            url: '/cases/',
            data: data,
            success: function(response)
            {
                $('#case-form').css('display', 'none');
                location.reload();
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