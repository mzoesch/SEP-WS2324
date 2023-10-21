@echo on
cd src/main/java/
javac app/App.java -d ../../../bin
cd ../../../bin
java app.App
cd ..
pause
