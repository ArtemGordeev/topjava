curl http://localhost:8080/topjava/rest/meals

curl http://localhost:8080/topjava/rest/meals/100002

curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31

curl --header "Content-Type: application/json" --request POST --data "{\"dateTime\":\"2020-03-30T10:00:00\",\"description\":\"newmeal\",\"calories\":500}" http://localhost:8080/topjava/rest/meals

curl --header "Content-Type: application/json" --request PUT --data "{\"id\":100002,\"dateTime\":\"2020-01-30T10:00:00\",\"description\":\"new breakfast\",\"calories\":200}" http://localhost:8080/topjava/rest/meals/100002

curl --request DELETE http://localhost:8080/topjava/rest/meals/100002