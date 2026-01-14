@echo off
echo --- Starting application ---

docker-compose down
docker-compose up -d --build

echo -- Application is up! ---
echo Application access: http://localhost:8080
echo Logs of application available with command: docker logs -f spring-planner-app
pause