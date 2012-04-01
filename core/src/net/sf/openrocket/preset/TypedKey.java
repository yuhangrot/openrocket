package net.sf.openrocket.preset;

import net.sf.openrocket.database.Databases;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.rocketcomponent.ExternalComponent.Finish;
import net.sf.openrocket.startup.Application;

public class TypedKey<T> {

	private final String name;
	private final Class<T> type;
	
	public TypedKey(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public Object parseFromString( String value ) {
		if ( type.equals(Boolean.class) ) {
			return Boolean.parseBoolean(value);
		}
		if ( type.isAssignableFrom(Double.class) ) {
			return Double.parseDouble(value);
		}
		if ( type.equals(String.class ) ) {
			return value;
		}
		if ( type.equals(Finish.class) ) {
			return Finish.valueOf(value);
		}
		if ( type.equals(Material.class) ) {
			// need to translate the value first!
			String translated_value = Application.getTranslator().get(value);
			Material material;
			material = Databases.findMaterial(Material.Type.BULK, translated_value);
			if ( material != null ) {
				return material;
			}
			material = Databases.findMaterial(Material.Type.LINE, translated_value);
			if ( material != null ) {
				return material;
			}
			material = Databases.findMaterial(Material.Type.SURFACE, translated_value);
			if ( material != null ) {
				return material;
			}
			throw new IllegalArgumentException("Invalid material " + value + " in component preset.");
		}
		throw new IllegalArgumentException("Inavlid type " + type.getName() + " for component preset parameter " + name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypedKey other = (TypedKey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
