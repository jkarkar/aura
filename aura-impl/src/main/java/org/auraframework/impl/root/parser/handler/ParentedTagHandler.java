/*
 * Copyright (C) 2013 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.auraframework.impl.root.parser.handler;

import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.auraframework.adapter.ConfigAdapter;
import org.auraframework.adapter.DefinitionParserAdapter;
import org.auraframework.def.ComponentDefRef;
import org.auraframework.def.DefDescriptor;
import org.auraframework.def.Definition;
import org.auraframework.def.HtmlTag;
import org.auraframework.impl.util.TextTokenizer;
import org.auraframework.service.DefinitionService;
import org.auraframework.system.TextSource;
import org.auraframework.throwable.quickfix.QuickFixException;
import org.auraframework.util.AuraTextUtil;

/**
 * Tag handler has a parent
 */
public abstract class ParentedTagHandler<T extends Definition, P extends Definition> extends ContainerTagHandler<T> {

    private ContainerTagHandler<P> parentHandler;

    public ParentedTagHandler() {
        super();
    }

    public ParentedTagHandler(ContainerTagHandler<P> parentHandler, XMLStreamReader xmlReader, TextSource<?> source,
                              boolean isInInternalNamespace, DefinitionService definitionService,
                              ConfigAdapter configAdapter, DefinitionParserAdapter definitionParserAdapter) {
        this(null, parentHandler, xmlReader, source, isInInternalNamespace, definitionService, configAdapter, definitionParserAdapter);
    }

    public ParentedTagHandler(DefDescriptor<T> defDescriptor, ContainerTagHandler<P> parentHandler, XMLStreamReader xmlReader, TextSource<?> source,
                              boolean isInInternalNamespace, DefinitionService definitionService,
                              ConfigAdapter configAdapter, DefinitionParserAdapter definitionParserAdapter) {
        super(defDescriptor, xmlReader, source, isInInternalNamespace, definitionService, configAdapter, definitionParserAdapter);
        this.parentHandler = parentHandler;
    }

    protected ContainerTagHandler<P> getParentHandler() {
        return parentHandler;
    }

    protected DefDescriptor<P> getParentDefDescriptor(){
        return parentHandler.getDefDescriptor();
    }

    @Override
    public boolean isInInternalNamespace() {
        return parentHandler != null && parentHandler.isInInternalNamespace();
    }

    protected List<ComponentDefRef> tokenizeChildText() throws XMLStreamException, QuickFixException {
        String text = xmlReader.getText();

        boolean skip = StringUtils.isBlank(text);

        if (!skip) {
            TextTokenizer tokenizer = TextTokenizer.tokenize(text, getLocation());
            return tokenizer.asComponentDefRefs(parentHandler);
        }
        return Collections.emptyList();
    }
}
