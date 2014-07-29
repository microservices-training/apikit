/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit.validation.cache;

import com.google.common.cache.CacheLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.raml.model.Raml;
import org.xml.sax.SAXException;

public class XmlSchemaCacheLoader extends CacheLoader<String, Schema>
{

    private Raml api;

    public XmlSchemaCacheLoader(Raml api)
    {
        this.api = api;
    }

    @Override
    public Schema load(String schemaLocation) throws IOException, SAXException
    {
        InputStream is = new ByteArrayInputStream(SchemaCacheUtils.resolveSchema(schemaLocation, api).getBytes());
        return compileSchema(is);
    }

    private static Schema compileSchema(InputStream inputStream) throws SAXException
    {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        return factory.newSchema(new StreamSource(inputStream));
    }
}
