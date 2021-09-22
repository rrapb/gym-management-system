// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

var labelsPieChartData = [];
var graphPieChartData = [];

$.ajax({
  type: "POST",
  url: "/chart/pie",
  data: null,
  cache: false,
  success: function (result) {
    for (let x in result) {
      labelsPieChartData.push(x);
      graphPieChartData.push(result[x])
    }
  },
  error: function (result) {
    console.log(result);
  }
});

// Pie Chart
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: labelsPieChartData,
    datasets: [{
      data: graphPieChartData,
      // backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
      // hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],

      backgroundColor: ['#003f7c', '#6b4490', '#b4418c', '#eb4a70', '#ff7146', '#ffa600'],
      hoverBackgroundColor: ['#003f5c', '#444e86', '#955196', '#dd5182', '#ff6e54', '#ffa600'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 80,
  },
});
var y = 0;
var pieTrigger = setInterval(function() {
  myPieChart.update();
  if(y === 15) {
    clearInterval(pieTrigger);
  }
  y++;
}, 100);