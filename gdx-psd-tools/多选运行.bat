@Echo Off
setlocal enabledelayedexpansion
TITLE  xlsx生成json工具
:start
CLS
COLOR 2f
mode con cols=50 lines=20

set format=√
set merge=×
set md5=√
set throwEmptyData=×
set config=config.ini

setlocal enabledelayedexpansion
for /f "delims=" %%i in ('type %config%^| find /i "="') do set %%i

:sc_main
cls

echo ----------------------------------------
echo    		多选
echo ----------------------------------------
echo.&echo.
echo             0. 导出数据
echo             1. 格式化 %format%
echo             2. 合并JSON %merge%
echo             3. MD5 %md5%
echo             4. 丢弃空数据 %throwEmptyData%
echo             5. 退出
echo             6. 帮助
set "select="
set/p select= 输入数字，按回车继续 :
if "%select%"=="0" (goto sc_ip0) 
if "%select%"=="1" (goto sc_ip1) 
if "%select%"=="2" (goto sc_ip2)
if "%select%"=="3" (goto sc_ip3) 
if "%select%"=="4" (goto sc_ip4) 
if "%select%"=="5" (goto sc_exit) 
if "%select%"=="6" (goto sc_help) 
:sc_exit
exit

goto :eof

:sc_ip0
cls
echo 开始导出 JSON
set _format=true
set _merge=true
set _md5=true
set _throwEmptyData=true
::set _merge=if "!merge!" == "√" (true) else (false)
::set _md5=if "!md5!" == "√" (true) else (false)


if "!format!" == "√" (set _format=true) else (set _format=false)
if "!merge!" == "√" (set _merge=true) else (set _merge=false)
if "!md5!" == "√" (set _md5=true) else (set _md5=false)
if "!throwEmptyData!" == "√" (set _throwEmptyData=true) else (set _throwEmptyData=false)

echo 保存配置
echo format=%format%>>tmp.ini
echo md5=%md5%>>tmp.ini
echo merge=%merge%>>tmp.ini
echo throwEmptyData=%throwEmptyData%>>tmp.ini
copy tmp.ini %config% /y >nul||(attrib -s -a -r -h %config%& copy tmp.ini %config% /y >nul)
del tmp.ini

echo java -jar ../xlsx2json.jar xlsx2json=true format=%_format% merge=%_merge% md5=%_md5% throwEmptyData=%_throwEmptyData%
java -jar ../xlsx2json.jar xlsx2json=true format=%_format% merge=%_merge% md5=%_md5% throwEmptyData=%_throwEmptyData%
echo JSON导出完成，任意键退出
PAUSE >nul
Goto sc_main


:sc_ip1
if "!format!" == "√" (
echo YES
set format=×
) else (
echo NO
set format=√
)
Goto sc_main
PAUSE >nul

:sc_ip2
if "!merge!" == "√" (
echo YES
set merge=×
) else (
echo NO
set merge=√
)
Goto sc_main
PAUSE >nul


:sc_ip3
if "!md5!" == "√" (
echo YES
set md5=×
) else (
echo NO
set md5=√
)
Goto sc_main
PAUSE >nul


:sc_ip4
if "!throwEmptyData!" == "√" (
echo YES
set throwEmptyData=×
) else (
echo NO
set throwEmptyData=√
)
Goto sc_main
PAUSE >nul



:sc_help
cls
echo 当 json 作为主配置文件的时候
echo 在第一个单元格内写入配置：
echo Config,Key=0,Value=1
echo Config 必选项，Key 和 Value 为可选项
PAUSE >nul
