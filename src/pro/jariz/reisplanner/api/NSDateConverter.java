/*
 * Copyright 2013 Jari Zwarts
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pro.jariz.reisplanner.api;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JARIZ.PRO
 * Created @ 20/06/13
 * By: JariZ
 * Project: Reisplanner2
 * Package: pro.jariz.reisplanner.api
 */
public class NSDateConverter implements Converter {
    public NSDateConverter() {
        super();
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return format.parse(reader.getValue());
        } catch (Exception ez) {
            throw new ConversionException(ez.getMessage(), ez);
        }
    }

    public boolean canConvert(Class clas) {
        return Date.class.isAssignableFrom(clas);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        //we don't generate xml in this app...
    }
}
