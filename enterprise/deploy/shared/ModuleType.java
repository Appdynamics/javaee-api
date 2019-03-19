/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.enterprise.deploy.shared;

/**
 * Class ModuleTypes defines enumeration values for the Java EE
 * module types.
 *
 * @author Rebecca Searls
 */
public class ModuleType 
{
	private int value; // This enumeration value's int value

	/**
	 * The module is an EAR archive.
	 */
	public static final ModuleType EAR = new ModuleType(0);
	/**
	 * The module is an Enterprise Java Bean archive.
	 */
	public static final ModuleType EJB = new ModuleType(1);
	/**
	 * The module is an Client Application archive.
	 */
	public static final ModuleType CAR = new ModuleType(2);
	/**
	 * The module is an Connector archive.
	 */
	public static final ModuleType RAR = new ModuleType(3);
	/**
	 * The module is an Web Application archive.
	 */
	public static final ModuleType WAR = new ModuleType(4);


	private static final String[] stringTable = {
	"ear",
	"ejb",
	"car",
	"rar",
	"war",
	};

	private static final ModuleType[] enumValueTable = {
	EAR,
	EJB,
	CAR,
	RAR,
	WAR,
	};

	/*
	 *  Module extensions.
	 */
    private static final String[] moduleExtension = {
    ".ear",
	".jar",
	".jar",
	".rar",
	".war",
    };
        

    /**
     * Construct a new enumeration value with the given integer value.
     *
     * @param  value  Integer value.
     */
    protected ModuleType(int value)
    {   this.value = value;
    }
        
    /**
     * Returns this enumeration value's integer value.
     * @return the value
     */
    public int getValue() 
	{ 	return value;
    }

        
	/**
	 * Returns the string table for class ModuleType
	 */
	protected String[] getStringTable()
	{   return stringTable;
	}

	/**
	 * Returns the enumeration value table for class ModuleType
	 */
	protected ModuleType[] getEnumValueTable()
	{   return enumValueTable;
	}

	/**
     * Return the file extension string for this enumeration.
     */
    public String getModuleExtension()
    {   return (moduleExtension[getValue()]);
    }
    
	/**
     * Return an object of the specified value.
     * @param value a designator for the object.
     */
    public static ModuleType getModuleType(int value)
    {   return enumValueTable[value];
    }

	/**
	 * Return the string name of this ModuleType or the
	 * integer value if outside the bounds of the table
	 */
    public String toString() 
	{
    	String[] strTable = getStringTable();
    	int index = value - getOffset();
		if (strTable != null && index >= 0 && index < strTable.length)
        	return strTable[index];
		else
        	return Integer.toString (value);
    }

    /**
     * Returns the lowest integer value used by this enumeration value's
     * enumeration class.
     * <P>
     * The default implementation returns 0. If the enumeration class (a
     * subclass of class EnumSyntax) uses integer values starting at other than

     * 0, override this method in the subclass.
     * @return the offset of the lowest enumeration value.
     */
    protected int getOffset() 
	{ 	return 0;
    }
} 
