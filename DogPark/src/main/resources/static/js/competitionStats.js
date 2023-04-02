let loadStatsBtn = document.getElementById("loadStats");

loadStatsBtn.addEventListener('click', reloadStats)

function reloadStats() {

    let statsCntr = document.getElementById('stats-container')
    statsCntr.innerHTML = ''

    fetch("http://localhost:8080/competitions/stats").
    then(response => response.json()).
    then(json => json.forEach(dog => {
        statsCntr.innerHTML += dogStatsAsHtml(dog)
    }))
}

function dogStatsAsHtml(dog) {
    let dogStatsHtml = '<tr class="row">\n'
    dogStatsHtml += `<td class="col-md-8">${dog.name}</td>\n`
    dogStatsHtml += `<td class="col-md-2">${dog.money}</td>\n`
    dogStatsHtml += `<td class="col-md-2">${dog.award}</td>\n`
    dogStatsHtml += '</tr>\n'

    return dogStatsHtml
}