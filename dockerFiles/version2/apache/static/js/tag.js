// load form for creating new tag
$(document).on("click", "#add_tag", function () {
    event.preventDefault();
    loadModal();
    $(document).off("click", "#send_tag");

    // send new tag to server
    $(document).on("click", "#send_tag", function () {
        event.preventDefault();
        sendTag('result_form', 'tag');
    });
});
// animation to display settings
$(document).on('click', ".manage-tag", function (e) {
    e.preventDefault();
    let id = $(this).data("menu");
    $(this).toggleClass('menu-btn_active');
    $('#menu_' + id).toggleClass('menu_active');
});
// load form for update tag
$(document).on("click", ".menu-block .manage .edit", function (event) {
    event.preventDefault();
    let id = $(this).data("tag-update-id");
    getFormForUpdateTag(id);
    $(document).on("click", "#send_tag", function () {
        event.preventDefault();
        updateTag('result_form', 'tag')
    });
});
// confirm to delete tag
$(document).on("click", ".menu-block .manage .delete", function (event) {
    event.preventDefault();
    let id = $(this).data("tag-delete-id");
    $('#dangerModal').modal();
    var modalConfirm = function (callback) {
        $("#btn-confirm").on("click", function () {
            $("#dangerModal").modal('show');
        });
        $("#modal-btn-si").on("click", function () {
            callback(true);
            $("#dangerModal").modal('hide');
        });
        $("#modal-btn-no").on("click", function () {
            callback(false);
            $("#dangerModal").modal('hide');
        });
    };
    modalConfirm(function (confirm) {
        if (confirm) {
            deleteTag(id);
        }
    });
});

function sendTag(result_form, ajax_form) {
    $.ajax({
        url: "/blog/tags/add",
        type: "POST",
        dataType: "html",
        data: $("#" + ajax_form).serialize(),
        success: function (data) {
            $('#modalTagForm').modal('toggle');
            $('#modalTagForm').on('hidden.bs.modal', function () {
                $('#modal').html(data);
                $('#centralModalSuccess').modal();
            });
        },
        error: function (response) {
        }
    });
}

function loadModal() {
    $.ajax({
        url: "/blog/tags/add", type: "GET", success: function (data) {
            if (data !== null) {
                $('#modal').html(data);
                $('#modalTagForm').modal();
            }
        }, error: function (jqXHR, testStatus, errorThrown) {
            this.alert(errorThrown);
        }
    });
}

function deleteTag(id) {
    $.ajax({
        url: "/blog/tags/" + id, type: "DELETE", dataType: "html",
        success: function (data) {
            $('#modal').html(data);
            $('#centralModalSuccess').modal();
            location.reload(true);
        }, error: function (response) {
        }
    });
}

function updateTag(result_form, ajax_form) {
    $.ajax({
        url: "/blog/tags/" + $("input[name='id']").val() + "/update",
        type: "PUT",
        dataType: "html",
        data: $("#" + ajax_form).serialize(),
        success: function (data) {
            $('#modalTagForm').modal('toggle');
            $('#modalTagForm').on('hidden.bs.modal', function () {
                $('#modal').html(data);
                $('#centralModalSuccess').modal();
            });
        },
        error: function (response) {
        }
    });
}

function getFormForUpdateTag(id) {
    $.ajax({
        url: "/blog/tags/" + id + "/update", type: "GET", success: function (data) {
            if (data !== null) {
                $('#modal').html(data);
                $('#modalTagForm').modal();
            }
        }, error: function (jqXHR, testStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}