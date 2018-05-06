/*
*/

let enabledHoursArr = [];
for (i = 0; i < 24; i++) {
    enabledHoursArr.push(i);
}

$(document).ready(function()
		{
			$('#date-format').bootstrapMaterialDatePicker
			({
				format: 'dddd DD MMMM YYYY - HH:mm', 
                nowButton: true,
                clearButton: true, 
                minDate : new Date(),
                enabledHours: enabledHoursArr,
                stepping: 15,
                minuteStep: 30,
                shortTime: true,
                okText: "next"
			});
			$.material.init()
		});
