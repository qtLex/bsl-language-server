/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2019
 * Alexey Sosnoviy <labotamy@yandex.ru>, Nikita Gryzlov <nixel2007@gmail.com>
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
package org.github._1c_syntax.bsl.languageserver.diagnostics;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;
import org.github._1c_syntax.parser.BSLLexer;
import org.github._1c_syntax.parser.BSLParser;

import java.util.Collection;

public class FunctionShouldHaveReturnDiagnostic extends AbstractVisitorDiagnostic {

  @Override
  public ParseTree visitFunction(BSLParser.FunctionContext ctx) {
    Collection<ParseTree> tokens = Trees.findAllTokenNodes(ctx, BSLLexer.RETURN_KEYWORD);
    if (tokens.isEmpty()) {
      BSLParser.SubNameContext subName = ctx.funcDeclaration().subName();
      addDiagnostic(subName);
    }
    return ctx;
  }

}