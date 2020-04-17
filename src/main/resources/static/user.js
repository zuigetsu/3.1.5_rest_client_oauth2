$(document).ready(function () {
    userList()
});

function userList() {
    let $users = $('#userList');
    $('#userList').empty();
    $('#modals').empty();

    $.ajax({
        type: 'GET',
        url: '/api/user',
        dataType: 'json',
        contentType: 'application/json',
        success: function(users) {
            $.each(users, function(i, user) {
                $users.append(userToRow(user));
                $("#modals").append(userToModal(user));
                $.each(user.roles, function(i, role) {
                    if (role.name == "ADMIN") {
                        $(`#rAdmin${user.id}`).attr("checked", "checked");
                    } else if (role.name == "USER") {
                        $(`#rUser${user.id}`).attr("checked", "checked");
                    }
                })
            });
        }
    });
};

function userToRow(user) {
    let roles = "";
    $.each(user.roles, function(i, role){
        roles += role.name + '; ';
    });
    return `<tr>
                <td scope="row"> ${user.id} </td>
                <td scope="row"> ${user.username} </td>
                <td scope="row"> ${user.age} </td>
                <td scope="row"> ${roles} </td>
                <td scope="row"> ${user.password} </td>
                <td><button class="btn btn-primary" data-toggle="modal" data-target="#useredit${user.id}">Edit</button></td>
                <td scope="row"><button class="btn btn-primary" onclick="deleteUser(${user.id})" value="${user.id}"> Delete </button></td>
            </tr>`;
}

function userToModal(user) {
    return `<div class="modal fade" id="useredit${user.id}" tabindex="-1" role="dialog" aria-labelledby="usereditLabel" aria-hidden="true">
                <div class="modal-dialog modal-sm" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Edit user ${user.username}</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>                            
                        </div>
                     <div class="modal-body text-center">
                        <label for="id${user.id}" class="font-weight-bold">ID</label>
                        <input type="text" class="form-control" name="id" id="id${user.id}" placeholder="ID" value="${user.id}" readonly/>
                        
                        <label for="name${user.id}" class="font-weight-bold">Username</label>
                        <input type="text" class="form-control" name="username" id="name${user.id}" placeholder="username" value="${user.username}"/>
                        
                        <label for="age${user.id}" class="font-weight-bold">Age</label>
                        <input type="text" class="form-control" name="age" id="age${user.id}" placeholder="age" value="${user.age}"/>
        
                        <label class="font-weight-bold">Roles</label>
                        <div class="row row-fluid justify-content-center">
                            <div class="col"><input type="checkbox" id="rAdmin${user.id}"  class="form-check-input" name="userRoles" value="ADMIN"><label for="rAdmin${user.id}">Admin</label></div>
                            <div class="col"><input type="checkbox" id="rUser${user.id}" class="form-check-input" name="userRoles" value="USER"><label for="rUser${user.id}">User</label></div>
                        </div>
    
                        <label for="password${user.id}" class="font-weight-bold">Password</label>
                        <input type="text" class="form-control" name="password" id="password${user.id}" placeholder="password"/>                                             
                     </div> 
                     <div class="modal-footer">
                        <button type="button"  class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                        <button type="button" class="btn btn-primary" onclick="updateUser(${user.id})" data-dismiss="modal">Edit user</button>   
                     </div>
                       
                    </div>
                </div>
             </div>`;
}

function addUser () {
    let arrayRoles = [];
    $("#addUserForm input:checkbox:checked").each(function(){
        if ($(this).val()=='ADMIN') {
            arrayRoles.push({id: "1", name: $(this).val()});
        } else {
            arrayRoles.push({id: "2", name: $(this).val()});
        }
    });

    var user = {
        username:  $("#addUserForm #username").val(),
        age:  $('#addUserForm #userage').val(),
        password:  $("#addUserForm #userpassword").val(),
        roles: arrayRoles
    }

    $.ajax({
        type: 'POST',
        url: '/api/user',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $("#addUserForm .form-control").each(function() {$(this).val('')})
            $("#addUserForm :checkbox").each(function() {$(this).prop( "checked", false )})
            alert(`User was ${user.username} added`);
            setTimeout(userList(), 1000);
            $("#nav-home-tab").click()
        },
        data: JSON.stringify(user)
    })
};

function deleteUser(id) {
    $.ajax({
        type: 'DELETE',
        url: '/api/user/' + id,
        success: function(data){
            alert(data);
            setTimeout(userList(), 1000);
        }
    })
}

function updateUser(id) {
    let arrayRoles = [];

    $(`#useredit${id} input:checkbox:checked`).each(function(){
        if ($(this).val()=='ADMIN') {
            arrayRoles.push({id: "1", name: $(this).val()});
        } else {
            arrayRoles.push({id: "2", name: $(this).val()});
        }
    });

    var user = {
        id: id,
        username:  $(`#name${id}`).val(),
        age:  $(`#age${id}`).val(),
        password:  $(`#password${id}`).val(),
        roles: arrayRoles
    }

    $.ajax({
        type: 'PATCH',
        url: '/api/user/' + id,
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            setTimeout(userList(), 1000);
        },
        data: JSON.stringify(user)
    })
}