@echo off

REM --------------------------------------------------
REM Monster Trading Cards Game
REM --------------------------------------------------
title Monster Trading Cards Game
echo CURL Testing for Monster Trading Cards Game
echo.


REM --------------------------------------------------
echo 17) battle
start /b "kienboec battle" curl -X POST http://localhost:10002/battles --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.
start /b "altenhof battle" curl -X POST http://localhost:10002/battles --header "Authorization: Basic altenhof-mtcgToken"
ping localhost -n 10 >NUL 2>NUL









REM --------------------------------------------------
echo end...

REM this is approx a sleep 
ping localhost -n 100 >NUL 2>NUL
@echo on