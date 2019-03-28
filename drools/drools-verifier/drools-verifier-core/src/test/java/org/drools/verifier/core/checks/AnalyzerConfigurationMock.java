/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.verifier.core.checks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.drools.verifier.core.checks.base.JavaCheckRunner;
import org.drools.verifier.core.configuration.AnalyzerConfiguration;
import org.drools.verifier.core.configuration.CheckConfiguration;
import org.drools.verifier.core.configuration.DateTimeFormatProvider;
import org.drools.verifier.core.index.keys.UUIDKeyProvider;

public class AnalyzerConfigurationMock
        extends AnalyzerConfiguration {

    public AnalyzerConfigurationMock() {
        this(CheckConfiguration.newDefault());
    }

    public AnalyzerConfigurationMock(final CheckConfiguration checkConfiguration) {
        super("UUID",
              new DateTimeFormatProvider() {
                  @Override
                  public String format(final Date dateValue) {
                      return new SimpleDateFormat("dd-MMM-yyyy").format(dateValue);
                  }
              },
              new UUIDKeyProvider() {

                  private long index = Long.SIZE;

                  @Override
                  protected String newUUID() {
                      return Long.toString(index--);
                  }
              },
              checkConfiguration,
              new JavaCheckRunner());
    }
}

