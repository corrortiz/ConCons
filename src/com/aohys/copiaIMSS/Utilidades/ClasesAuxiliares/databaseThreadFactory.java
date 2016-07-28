/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * clase para crear hilos de la base de datos
 * @author CorrO
 */
public class databaseThreadFactory implements ThreadFactory {

   static final AtomicInteger poolNumber = new AtomicInteger(1);
        @Override 
        public Thread newThread(Runnable runnable) {
          Thread thread = new Thread(runnable, "Database-Connection-" + poolNumber.getAndIncrement() + "-thread");
          thread.setDaemon(true);
          return thread;
        }

}
