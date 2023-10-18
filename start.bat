@echo on
cd src/
javac app/App.java -d ../bin
cd ../bin
java app.App
cd ..
pause
