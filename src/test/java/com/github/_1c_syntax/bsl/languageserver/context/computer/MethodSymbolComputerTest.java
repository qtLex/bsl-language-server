/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.context.computer;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.context.symbol.ParameterDefinition;
import com.github._1c_syntax.bsl.languageserver.context.symbol.annotations.Annotation;
import com.github._1c_syntax.bsl.languageserver.context.symbol.annotations.CompilerDirective;
import com.github._1c_syntax.bsl.languageserver.util.TestUtils;
import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodSymbolComputerTest {

  @Test
  void testMethodSymbolComputer() {

    DocumentContext documentContext = TestUtils.getDocumentContextFromFile("./src/test/resources/context/computer/MethodSymbolComputerTest.bsl");
    List<MethodSymbol> methods = documentContext.getSymbolTree().getMethods();

    assertThat(methods.size()).isEqualTo(3);

    assertThat(methods.get(0).getName()).isEqualTo("Один");
    assertThat(methods.get(0).getDescription().orElse(null)).isNull();
    assertThat(methods.get(0).getRange()).isEqualTo(Ranges.create(1, 0, 3, 14));
    assertThat(methods.get(0).getSubNameRange()).isEqualTo(Ranges.create(1, 10, 1, 14));

    assertThat(methods.get(1).getDescription()).isNotEmpty();
    assertThat(methods.get(1).getRegion().orElse(null).getName()).isEqualTo("ИмяОбласти");
    assertThat(methods.get(1).getDescription().orElse(null).getDescription()).isNotEmpty();

  }

  @Test
  void testParameters() {

    DocumentContext documentContext = TestUtils.getDocumentContextFromFile("./src/test/resources/context/computer/MethodSymbolComputerTest.bsl");
    List<MethodSymbol> methods = documentContext.getSymbolTree().getMethods();

    List<ParameterDefinition> parameters = methods.get(2).getParameters();
    assertThat(parameters.size()).isEqualTo(4);
    assertThat(parameters.get(0).getName()).isEqualTo("Парам");
    assertThat(parameters.get(0).isByValue()).isFalse();
    assertThat(parameters.get(0).isOptional()).isFalse();

    assertThat(parameters.get(1).getName()).isEqualTo("Парам2");
    assertThat(parameters.get(1).isByValue()).isTrue();
    assertThat(parameters.get(1).isOptional()).isFalse();

    assertThat(parameters.get(2).getName()).isEqualTo("Парам3");
    assertThat(parameters.get(2).isByValue()).isFalse();
    assertThat(parameters.get(2).isOptional()).isTrue();

    assertThat(parameters.get(3).getName()).isEqualTo("Парам4");
    assertThat(parameters.get(3).isByValue()).isTrue();
    assertThat(parameters.get(3).isOptional()).isTrue();

  }

  @Test
  void testParseError() {

    DocumentContext documentContext = TestUtils.getDocumentContextFromFile("./src/test/resources/context/computer/MethodSymbolComputerTestParseError.bsl");
    List<MethodSymbol> methods = documentContext.getSymbolTree().getMethods();

    assertThat(methods.get(0).getName()).isEqualTo("Выполнить");
    assertThat(methods.get(0).getSubNameRange()).isEqualTo(Ranges.create(0, 10, 0, 19));

  }

  @Test
  void testCompilerDirective() {

    String module = "&НаКлиенте\n" +
      "Процедура Метод()\n" +
      "КонецПроцедуры";

    List<MethodSymbol> methods = getMethodSymbols(module);

    assertThat(methods).hasSize(1);
    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getName()).isEqualTo("Метод");
    assertThat(methodSymbol.getCompilerDirective().orElse(null)).isEqualTo(CompilerDirective.AT_CLIENT);
    assertThat(methodSymbol.getAnnotations()).hasSize(0);
  }

  @Test
  void testCompilerDirectiveAtServerNoContext() {

    String module = "&НаСервереБезКонтекста\n" +
      "Процедура Метод()\n" +
      "КонецПроцедуры";

    List<MethodSymbol> methods = getMethodSymbols(module);

    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getName()).isEqualTo("Метод");
    assertThat(methodSymbol.getCompilerDirective().orElse(null)).isEqualTo(CompilerDirective.AT_SERVER_NO_CONTEXT);
    assertThat(methodSymbol.getAnnotations()).hasSize(0);
  }

  @Test
  void testSeveralCompilerDirective() {

    String module = "&НаКлиенте\n&НаСервереБезКонтекста\n" +
      "Процедура Метод()\n" +
      "КонецПроцедуры";

    List<MethodSymbol> methods = getMethodSymbols(module);

    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getName()).isEqualTo("Метод");
    assertThat(methodSymbol.getCompilerDirective().orElse(null)).isEqualTo(CompilerDirective.AT_CLIENT);
    assertThat(methodSymbol.getAnnotations()).hasSize(0);
  }

  @Test
  void testNonCompilerDirectiveAndNonAnnotation() {

    String module = "Процедура Метод()\n" +
      "КонецПроцедуры";

    List<MethodSymbol> methods = getMethodSymbols(module);

    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getName()).isEqualTo("Метод");
    assertThat(methodSymbol.getCompilerDirective().isPresent()).isEqualTo(false);
    assertThat(methodSymbol.getAnnotations()).hasSize(0);
  }

  @Test
  void testAnnotation() {

    String module = "&После\n" +
      "Процедура Метод()\n" +
      "КонецПроцедуры";

    List<MethodSymbol> methods = getMethodSymbols(module);

    assertThat(methods).hasSize(1);
    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getCompilerDirective().isPresent()).isEqualTo(false);
    var annotations = methodSymbol.getAnnotations();
    assertThat(annotations).hasSize(1);
    assertThat(annotations.get(0)).isEqualTo(Annotation.AFTER);
  }

  @Test
  void testCompilerDirectiveAndAnnotation() {

    String module = "&НаКлиенте\n&После\n" +
      "Процедура Метод()\n" +
      "КонецПроцедуры";

    checkCompilerDirective_AtClient_AndAnnotation_After(module);
  }

  @Test
  void testCompilerDirectiveAndAnnotationOtherOrder() {

    String module = "&После\n&НаКлиенте\n" +
      "Процедура Метод()\n" +
      "КонецПроцедуры";

    checkCompilerDirective_AtClient_AndAnnotation_After(module);
  }

  @Test
  void testCompilerDirectiveAndAnnotationForFunction() {

    String module = "&НаКлиенте\n&После\n" +
      "Функция Метод()\n" +
      "КонецФункции";

    checkCompilerDirective_AtClient_AndAnnotation_After(module);
  }

  @Test
  void testSeveralAnnotationsForFunction() {

    String module = "&Аннотация1\n" +
      "&Аннотация2\n" +
      "Процедура Метод() Экспорт\n" +
      "КонецПроцедуры";

    List<MethodSymbol> methods = getMethodSymbols(module);

    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getCompilerDirective().isPresent()).isEqualTo(false);
    var annotations = methodSymbol.getAnnotations();
    assertThat(annotations).hasSize(2);
    assertThat(annotations.get(0)).isEqualTo(Annotation.CUSTOM);
    assertThat(annotations.get(1)).isEqualTo(Annotation.CUSTOM);
  }

  private static void checkCompilerDirective_AtClient_AndAnnotation_After(String module) {
    List<MethodSymbol> methods = getMethodSymbols(module);

    assertThat(methods).hasSize(1);
    var methodSymbol = methods.get(0);
    assertThat(methodSymbol.getCompilerDirective().orElse(null)).isEqualTo(CompilerDirective.AT_CLIENT);
    var annotations = methodSymbol.getAnnotations();
    assertThat(annotations).hasSize(1);
    assertThat(annotations.get(0)).isEqualTo(Annotation.AFTER);
  }

  private static List<MethodSymbol> getMethodSymbols(String module) {
    DocumentContext documentContext = TestUtils.getDocumentContext(module);
    return documentContext.getSymbolTree().getMethods();
  }
}
