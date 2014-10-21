$(document).ready(function() {
    $('#users_list tbody tr:even').css(
        {'background-color': '#e6e6fa'}
    );
    $('#users_list tbody th').css(
        {'background-color': '#969696', 'color': 'black', 'border':'3px solid black'}
    );
    $('#users_list tbody').css(
        {'border':'3px solid black'}
    );
    $('#users_list tbody td').css(
        {'border-right':'3px solid black'}
    );

    if ($('#birthday').val() == '') {
        $('#birthday').val('YYYY-MM-DD');
    }
    $('#birthday').click(function() {
        if ($('#birthday').val() == 'YYYY-MM-DD') {
            $('#birthday').val('');
        }
    });
});

