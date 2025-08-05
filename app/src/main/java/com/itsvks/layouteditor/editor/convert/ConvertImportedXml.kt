package com.itsvks.layouteditor.editor.convert

import android.content.Context
import com.itsvks.layouteditor.editor.DesignEditor
import com.itsvks.layouteditor.tools.XmlLayoutGenerator
import com.itsvks.layouteditor.tools.XmlLayoutParser
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import androidx.core.view.isEmpty

class ConvertImportedXml(private val xml: String?) {

  /**
   * Converts an Android layout XML into the canonical format needed by the editor.
   * It ensures all tags are fully qualified class names and all namespaces are preserved.
   */
  fun getXmlConverted(context: Context): String? {
    if (xml.isNullOrBlank()) {
      return null
    }

    val tempEditor = DesignEditor(context)

    try {
      tempEditor.loadLayoutFromParser(xml)
    } catch (e: Exception) {
      e.printStackTrace()  // Null pointer exeption throwing
      /**
       * 0 = {StackTraceElement@41501} "com.itsvks.layouteditor.editor.DesignEditor.clearAll(DesignEditor.kt:435)"
       * 1 = {StackTraceElement@41502} "com.itsvks.layouteditor.editor.DesignEditor.loadLayoutFromParser(DesignEditor.kt:367)"
       * 2 = {StackTraceElement@41503} "com.itsvks.layouteditor.editor.convert.ConvertImportedXml.getXmlConverted(ConvertImportedXml.kt:25)"
       * 3 = {StackTraceElement@41504} "com.itsvks.layouteditor.activities.EditorActivity.androidToDesignConversion(EditorActivity.kt:190)"
       * 4 = {StackTraceElement@41505} "com.itsvks.layouteditor.activities.EditorActivity.init(EditorActivity.kt:134)"
       * 5 = {StackTraceElement@41506} "com.itsvks.layouteditor.activities.EditorActivity.onCreate(EditorActivity.kt:103)"
       * 6 = {StackTraceElement@41507} "android.app.Activity.performCreate(Activity.java:8616)"
       * 7 = {StackTraceElement@41508} "android.app.Activity.performCreate(Activity.java:8594)"
       * 8 = {StackTraceElement@41509} "android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1465)"
       * 9 = {StackTraceElement@41510} "android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3929)"
       * 10 = {StackTraceElement@41511} "android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:4087)"
       * 11 = {StackTraceElement@41512} "android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:114)"
       * 12 = {StackTraceElement@41513} "android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:139)"
       * 13 = {StackTraceElement@41514} "android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:96)"
       * 14 = {StackTraceElement@41515} "android.app.ActivityThread$H.handleMessage(ActivityThread.java:2560)"
       * 15 = {StackTraceElement@41516} "android.os.Handler.dispatchMessage(Handler.java:106)"
       * 16 = {StackTraceElement@41517} "android.os.Looper.loopOnce(Looper.java:243)"
       * 17 = {StackTraceElement@41518} "android.os.Looper.loop(Looper.java:338)"
       * 18 = {StackTraceElement@41519} "android.app.ActivityThread.main(ActivityThread.java:8524)"
       * 19 = {StackTraceElement@41520} "java.lang.reflect.Method.invoke(Native Method)"
       * 20 = {StackTraceElement@41521} "com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:602)"
       * 21 = {StackTraceElement@41522} "com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1064)"
       */
      return null
    }

    if (tempEditor.isEmpty()) {
      return null
    }

    val generator = XmlLayoutGenerator()
    return generator.generate(tempEditor, false)
  }
}
