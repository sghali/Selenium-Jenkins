Opt("MustDeclareVars", 1);0=no, 1=require pre-declare
Main()
Func Main()  
    Local Const $dialogTitle = $CmdLine[1]
    Local Const $timeout = 5
    Local $windowFound = WinWait($dialogTitle, "", $timeout)   
    $windowFound = WinWait($dialogTitle, "", $timeout)
    Local $windowHandle
    If $windowFound Then       
        $windowHandle = WinGetHandle("[LAST]")
        WinActivate($windowHandle)      
        ControlClick($windowHandle, "", "[CLASS:Button]")           
    Else     
        MsgBox(0, "", "Could not find window.")
        Exit(1)
     EndIf
EndFunc    
