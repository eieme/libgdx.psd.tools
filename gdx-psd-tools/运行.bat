@Echo Off
TITLE  xlsx����json����
:start
CLS
COLOR 2f
mode con cols=50 lines=20
:sc_main
echo ----------------------------------------
echo    			ѡ��
echo ----------------------------------------
echo.&echo.
echo             0.��ʽ��-----json
echo             1.δ��ʽ��---json
echo             2.�˳�
echo             3 ����
set "select="
set/p select= �������֣����س����� :
if "%select%"=="0" (goto sc_ip0) 
if "%select%"=="1" (goto sc_ip1)
if "%select%"=="2" (goto sc_exit) 
if "%select%"=="3" (goto sc_help) 
:sc_exit
exit
goto :eof
:sc_ip0
cls
echo �Ժ򵼳�,��ʽ��json
java -jar xlsx2json.jar format=true
echo ��ʽ��Json������ɣ�������˳�
PAUSE >nul
Goto sc_maina
:sc_ip1
cls
echo �Ժ򵼳�,δ��ʽ��json
java -jar xlsx2json.jar
echo δ��ʽ��Json������ɣ�������˳�
PAUSE >nul
Goto sc_maina
:sc_help
cls
echo �� json ��Ϊ�������ļ���ʱ��
echo �ڵ�һ����Ԫ����д�����ã�
echo Config,Key=0,Value=1
echo Config ��ѡ�Key �� Value Ϊ��ѡ��
Goto sc_main
