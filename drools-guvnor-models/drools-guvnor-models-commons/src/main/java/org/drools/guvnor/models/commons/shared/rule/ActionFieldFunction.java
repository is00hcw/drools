/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.guvnor.models.commons.shared.rule;

/**
 * This node indicates that the user wants to execute a method on some
 * fact in case the LHR matches.
 */
public class ActionFieldFunction extends ActionFieldValue {

    private volatile String method = "";

    public ActionFieldFunction() {
    }

    public ActionFieldFunction( final String field,
                                final String value,
                                final String type ) {
        super( field, value, type );
    }

    public void setMethod( final String methodName ) {
        this.method = methodName;
    }

    public String getMethod() {
        return this.method;
    }
}
