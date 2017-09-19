@Echo Off
TITLE  xlsx生成json工具
:start
CLS
COLOR 2f
mode con cols=50 lines=20
:sc_main
echo ----------------------------------------
echo    			选择
echo ----------------------------------------
echo.&echo.
echo             0.格式化-----json
echo             1.未格式化---json
echo             2.退出
set "select="
set/p select= 输入数字，按回车继续 :
if "%select%"=="0" (goto sc_ip0) 
if "%select%"=="1" (goto sc_ip1)
if "%select%"=="2" (goto sc_exit) 
:sc_exit
exit
goto :eof
:sc_ip0
cls
echo 稍候导出,格式化json
java -jar xlsx2json.jar format=true
echo 格式化Json导出完成，任意键退出
PAUSE >nul
Goto sc_maina
:sc_ip1
cls
echo 稍候导出,未格式化json
java -jar xlsx2json.jar
echo 未格式化Json导出完成，任意键退出
PAUSE >nul
Goto sc_maina