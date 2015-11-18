var text = new FilterConfig();

function makeGUI() {
    "use strict";
    var gui = new dat.GUI();
    gui.add(text, 'debug_type', ['permissions', 'flags', 'speeds']).onFinishChange(getStyle);

    $.ajax(url + "/stats", {
        dataType: 'JSON',
        success: function(data) {
            if (data.data) {
                $.each(data.data, function(key, usages) {
                    var split_name = key.replace("_", " ");
                    var nice_name = split_name.toProperCase();
                    var lowercase_name = key.toLowerCase()
                    console.log("key: ", split_name.toProperCase());
                    var tmp = gui.addFolder(nice_name);
                    tmp.add(text, 'show_' + lowercase_name)
                    .onFinishChange(function(value) {flagChange(lowercase_name, value)});
                    tmp.addColor(text, 'color_' + lowercase_name)
                    .onChange(function(color) {colorChange(lowercase_name, color)});

                });

                showFlagInfos(data);
            }else {
                console.error(data.errors);
                alert("Problem loading flags from server:" + data.errors);
            }
        }
    });


}