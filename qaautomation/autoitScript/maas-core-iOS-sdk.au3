Opt("MustDeclareVars", 1);0=no, 1=require pre-declare

Main()


Func Main()
  
    Local Const $dialogTitle = $CmdLine[2]
    Local Const $timeout = 5

    Local $windowFound = WinWait($dialogTitle, "", $timeout)
    
   
    $windowFound = WinWait($dialogTitle, "", $timeout)
    Local $windowHandle

    If $windowFound Then
       
        $windowHandle = WinGetHandle("[LAST]")
        WinActivate($windowHandle)
   
        ControlSetText($windowHandle, "", "[CLASS:Edit; INSTANCE:1]", $CmdLine[1])
        ControlClick($windowHandle, "", "[CLASS:Button; TEXT:&Open]")        
        
    Else
     
        MsgBox(0, "", "Could not find window.")
        Exit(1)
     EndIf
EndFunc    
