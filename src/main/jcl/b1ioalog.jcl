//B1IOALOG JOB (HST,02,LYE),'BITACORA BATCH',
//         CLASS=1,
//         MSGLEVEL=(1,1),
//   MSGCLASS=L,REGION=0M
//*********************************************************************
//*PASO DE COMPILACIÏN
//*********************************************************************
//PASO1   EXEC PROC=ASMACL
//C.SYSLIB   DD  DSN=SYS1.MACLIB,DISP=SHR
//           DD  DSN=ASM.SASMMAC2,DISP=SHR
//C.SYSIN    DD  DSN=M909381.ASM.SRC(FORMLOG),DISP=SHR
//L.SYSLMOD  DD  DSN=M909381.DATA.LOADW(FORMLOG),
//*L.SYSLMOD  DD  DSN=MBVD.LOADLIB.BATCH(FORMLOG),
//             DISP=SHR
//*             DISP=(NEW,KEEP,KEEP),UNIT=SYSALLDA,
//*             SPACE=(CYL,(1,1,1))
//
//*********************************************************************
//*INICIALIZACIÏN DE VARIABLES AUTOEDIT
//*PARA EL TIEMPO INICIAL SE TOMA UNA HORA ATRAS, EN CASO QUE LA HORA
//*ATRAS QUEDE EN EL D•A ANTERIOR, SE TOMAN LAS 00:00.
//*ADEMAS SI LA HORA TIENE SOLO UN D•GITO, SE LE PONE 0 COMO PREFIJO.
//*********************************************************************
//*%%SET %%TIEMPO_ACTUAL= %%TIME
//*%%SET %%HORA_FINAL= %%SUBSTR %%TIEMPO_ACTUAL 1 2
//*%%SET %%MINUTO_FINAL=%%SUBSTR %%TIEMPO_ACTUAL 3 2
//*%%SET %%HORA_INICIAL=%%HORA_FINAL %%MINUS 1
//*%%SET %%MINUTO_INICIAL=%%MINUTO_FINAL
//*%%SET %%HORA_INICIAL_STR=%%HORA_INICIAL
//*%%SET %%LONG_HORA=%%$LENGTH %%HORA_INICIAL
//*%%IF %%LONG_HORA EQ 1
//*    %%SET %%HORA_INICIAL_STR=0%%.%%HORA_INICIAL
//*%%ENDIF
//*%%IF %%HORA_INICIAL LT 0
//*    %%SET %%HORA_INICIAL_STR=00
//*    %%SET %%MINUTO_INICIAL=00
//*%%ENDIF
//*%%SET %%FECHA=%%YEAR.%%MONTH.%%DAY
//*%%SET %%TIEMPO_INICIAL=%%HORA_INICIAL_STR.%%MINUTO_INICIAL
//*%%SET %%TIEMPO_FINAL=%%HORA_FINAL.%%MINUTO_FINAL
//*********************************************************************
//*SE EXTRAE LA INFORMACIÏN DEL LOG IOA A TRAV©S DE IOAAPI EN VEZ DEL
//*ARCHIVO DIRECTAMENTE PARA NO CORRER EL RIESGO DE QUE NO SE PUEDA
//*OBTENER EL JOBID.
//*********************************************************************
//PASO2   EXEC PGM=IOAAPI
//LOGIOA   DD DSN=M909381.LOGIOA,
//         DISP=(OLD,KEEP,KEEP)
//*        DISP=(NEW,CATLG,DELETE),
//*        SPACE=(CYL,(5,3),RLSE),UNIT=3390,
//*       DCB=(DSORG=PS,LRECL=200,BLKSIZE=0,RECFM=FB)
//STEPLIB DD  DISP=SHR,DSN=SYS3.IOAO.V630.MEX1.TGT.LOAD
//        DD  DISP=SHR,DSN=SYS3.IOAO.V630.MEX1.TGT.LOADE
//DAAPIOUT  DD  SYSOUT=*
//DAPARM    DD  DISP=SHR,DSN=SYS3.IOAI.V630.MEX1.TGT.PARM
//          DD  DISP=SHR,DSN=SYS3.IOAI.V630.MEX1.TGT.IOAENV
//DALOG     DD  DISP=SHR,DSN=SYS3.IOAD.V630.SYSPBBV.DATA.LOG
//DAAPIPRM  DD *
     LOG DATE BETWEEN %%FECHA AND %%FECHA
     LOG TIME BETWEEN %%TIEMPO_INICIAL AND %%TIEMPO_FINAL
     LOG FILE LOGIOA
     LOG FIND
/*
//*********************************************************************
//*SE GENERA EL ARCHIVO DE LOG FORMATEADO CON LOS CAMPOS EMPRESA,
//*CÏDIGO, JOB, ODATE, OID, TIEMPO DEL EVENTO, PASO DE ERROR, JOBID,
//*CÏDIGO DE ERROR, GRUPO, ACCIÏN (ACCIÏN HUMANA), USUARIO
//*********************************************************************
//PASO3   EXEC PGM=FORMLOG,REGION=0M
//SYSPRINT DD  SYSOUT=*
//STEPLIB  DD  DSN=M909381.DATA.LOADW,DISP=SHR
//LOGFORM  DD  DSN=M909381.LOGFORM,
//             DISP=(OLD,KEEP,KEEP)
//*            DISP=(NEW,CATLG,CATLG),
//*            SPACE=(CYL,(1,1))
//LOGIOA   DD  DSN=M909381.LOGIOA,
//             DISP=SHR
//*
//*********************************************************************
//*SE ENVIA EL ARCHIVO DE LOG FORMATEADO AL SERVIDOR
//*********************************************************************
//PASO4    EXEC PGM=FTP,PARM='150.100.214.41 (EXIT=16'
//SYSIN    DD DUMMY
//OUTPUT   DD SYSOUT=*
//INPUT    DD *
monitoreo
ccr123
pwd
cd /home/monitoreo
PUT 'M909381.LOGFORM' logmex.csv
site chmod 666 logmex.csv
/*
