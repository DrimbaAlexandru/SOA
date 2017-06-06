var readConfig;

var race_remote = {
    read: {
        url: "http://localhost:8080/race",
        method: "GET"
    },
    modify: {
        create: function(items, success, error) {
            var item = items[0].data;
            $.ajax({
                url: "http://localhost:8080/race",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(item)
            });
        },
        update: function(items, success, error) {
            var item = items[0].data;
            $.ajax({
                url: "http://localhost:8080/race/" + item.id,
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(item),
                success: function() {
                    $("#grid-race").swidget().refresh();
                }
            });
        },
        remove: function(items, success, error) {
            var item = items[0].data;
            $.ajax({
                url: "http://localhost:8080/race/" + item.id,
                method: "DELETE",
                success: function() {
                    $("#grid-race").swidget().refresh();
                }
            })
        }
    }
};

var age_group_remote = {
    read: {
        url: "http://localhost:8080/agegroup",
        method: "GET"
    }
};

var race_data_source = {
    schema: {
        fields: {
            id: { path: "id", type: Number },
            length: { path: "length", type: String },
            start: { path: "start", type: Number },
            end: { path: "end", type: Number }
        }
    },
    remote: race_remote
};

var age_group_data_source = {
    schema: {
        fields: {
            id: { path: "id", type: Number },
            start: { path: "start", type: Number },
            end: { path: "end", type: Number }
        }
    },
    remote: age_group_remote

};

function initGrid(gridName, data_source, columns, editing_enabled) {
    var grid = $("#"+gridName+"");
    grid.shieldGrid({
        dataSource: data_source,
        sorting: {
            multiple: true
        },
        rowHover: false,
        columns: columns,
        editing: {
            enabled: editing_enabled,
            event: "click",
            type: "cell"
        },
        events:
            {
                getCustomEditorValue: function (e) {
                    e.value = $("#dropdown").swidget().value();
                    $("#dropdown").swidget().destroy();
                    console.log(e);
                }
            },
        paging: {
            pageSize: 15
        }
    });
}

$(document).ready(function () {
    initGrid("grid-race", race_data_source, [
            { field: "id", title: "Id", width: "80px" },
            { field: "length", title: "Race length", width: "120px" },
            { field: "start", title: "Start age", width: "80px" },
            { field: "end", title: "End age", width: "80px" },
            {
                width: "104px",
                title: "Delete Column",
                buttons: [
                    { cls: "deleteButton", commandName: "delete", caption: "<img src='http://www.prepbootstrap.com/Content/images/template/BootstrapEditableGrid/delete.png' /><span>Delete</span>" }
                ]
            }
        ], true);
    initGrid("grid-age-group", age_group_data_source, [
            { field: "id", title: "Age Group id", width: "120px" },
            { field: "start", title: "Start age", width: "80px" },
            { field: "end", title: "End age", width: "80px" }
        ], false);

    $("#goCreate").click(function() {

        var length = $("#createLength").val(),
            ageGroup = $("#createAgeGroupId").val();

        if(length === "" || ageGroup === "") {
            return;
        }

        $.ajax({
            url: "http://localhost:8080/race/",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                length: length,
                ageGroup: {
                    id: ageGroup
                }
            }),
            success: function() {
                $("#grid-race").swidget().refresh();
            }
        });
    });
});

