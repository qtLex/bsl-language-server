/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2021
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
package com.github._1c_syntax.bsl.languageserver.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Сборник общих Pointcut для AOP-слоя.
 */
public class Pointcuts {

  /**
   * Это обращение к одному из классов продукта.
   */
  @Pointcut("within(com.github._1c_syntax.bsl.languageserver..*)")
  public void isBSLLanguageServerScope() {
  }

  /**
   * Это обращение к классу LanguageServerConfiguration.
   */
  @Pointcut("within(com.github._1c_syntax.bsl.languageserver.configuration.LanguageServerConfiguration)")
  public void isLanguageServerConfiguration() {
  }

  /**
   * Это вызов метода update.
   */
  @Pointcut("isBSLLanguageServerScope() && execution(* update(..))")
  public void isUpdateCall() {
  }

  /**
   * Это вызов метода reset.
   */
  @Pointcut("isBSLLanguageServerScope() && execution(* reset(..))")
  public void isResetCall() {
  }
}
