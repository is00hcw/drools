/*
 * Copyright 2012 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.guvnor.models.commons.shared.auditlog;

import java.util.Map;

/**
 * An Audit Log Filter, controlling which entries passed to the AuditLog are
 * actually appended to the log.
 */
public interface AuditLogFilter {

    /**
     * Register a type this Filter understands. When a new entry is added the
     * AuditLogFilter is set to not accept the type by default.
     * @param type
     */
    public void addType( final String type );

    /**
     * This is the filtering method. When an AuditLogEntry is added to an
     * AuditLog the AuditLog calls this method to determine whether the
     * AuditLogEntry should be added.
     * @param entry
     * @return true if the AuditLogEntry should be added to the AuditLog
     */
    boolean accept( final AuditLogEntry entry );

    public Map<String, Boolean> getAcceptedTypes();

}
