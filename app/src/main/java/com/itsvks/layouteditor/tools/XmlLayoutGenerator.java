package com.itsvks.layouteditor.tools;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.itsvks.layouteditor.editor.DesignEditor;
import com.itsvks.layouteditor.editor.initializer.AttributeMap;
import org.apache.commons.text.StringEscapeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlLayoutGenerator {
  final StringBuilder builder = new StringBuilder();
  String TAB = "\t";
  boolean useSuperclasses;

  public String generate(@NonNull DesignEditor editor, boolean useSuperclasses) {
    this.useSuperclasses = useSuperclasses;

    if (editor.getChildCount() == 0) {
      return "";
    }
    builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    builder.append(
      """
        <!--
        \tWelcome to LayoutEditor!

        \tWe are proud to present our innovative layout generator app that
        \tallows users to create and customize stunning layouts in no time.
        \tWith LayoutEditor, you can easily create beautiful and custom
        \tlayouts that are tailored to fit your unique needs.

        \tThank you for using LayoutEditor and we hope you enjoy our app!
        -->

        """);

    // REMOVED: We no longer need the parser instance or a separate namespaces map.
    // XmlLayoutParser parser = editor.getParser();
    // Map<String, String> namespaces = (parser != null) ? parser.getNamespaceDeclarations() : new HashMap<>();

    // CHANGED: The call to peek is now simpler.
    return peek(editor.getChildAt(0), editor.getViewAttributeMap(), 0);
  }

  // CHANGED: The 'namespaces' map parameter is removed.
  private String peek(View view, HashMap<View, AttributeMap> attributeMap, int depth) {
    if (attributeMap == null || view == null) return "";
    String indent = getIndent(depth);
    int nextDepth = depth;

    String className = getClassName(view, indent);

    // REMOVED: This special block for namespaces is no longer needed.
    // The main attribute loop will handle them automatically.
    /*
    if (depth == 0) {
      if (namespaces != null && !namespaces.isEmpty()) {
        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
          builder.append(TAB)
                  .append("xmlns:")
                  .append(entry.getKey())
                  .append("=\"")
                  .append(entry.getValue())
                  .append("\"\n");
        }
      }
    }
    */

    // This loop now writes ALL attributes for the view. For the root view,
    // this will include xmlns:android, android:layout_width, etc.
    List<String> keys =
            (attributeMap.get(view) != null) ? attributeMap.get(view).keySet() : new ArrayList<>();
    for (String key : keys) {
      builder.append(TAB).append(indent).append(key).append("=\"").append(StringEscapeUtils.escapeXml11(attributeMap.get(view).getValue(key))).append("\"\n");
    }

    builder.deleteCharAt(builder.length() - 1);

    if (view instanceof ViewGroup) {
      ViewGroup group = (ViewGroup) view;
      if (!(group instanceof CalendarView)
              && !(group instanceof SearchView)
              && !(group instanceof NavigationView)
              && !(group instanceof BottomNavigationView)
              && !(group instanceof TabLayout)) {
        nextDepth++;

        if (group.getChildCount() > 0) {
          builder.append(">\n\n");

          for (int i = 0; i < group.getChildCount(); i++) {
            // CHANGED: The recursive call is also simplified.
            peek(group.getChildAt(i), attributeMap, nextDepth);
          }

          builder.append(indent).append("</").append(className).append(">\n\n");
        } else {
          builder.append(" />\n\n");
        }
      } else {
        builder.append(" />\n\n");
      }
    } else {
      builder.append(" />\n\n");
    }

    return builder.toString().trim();
  }

  @NonNull
  private String getClassName(View view, String indent) {
    String className =
            useSuperclasses ? view.getClass().getSuperclass().getName() : view.getClass().getName();

    if (useSuperclasses) {
      if (className.startsWith("android.widget.")) {
        className = className.replace("android.widget.", "");
      }
    }

    builder.append(indent).append("<").append(className).append("\n");
    return className;
  }

  @NonNull
  private String getIndent(int depth) {
    return TAB.repeat(depth);
  }
}