package jnode.install;

import java.util.Date;

import jnode.dto.Version;

public class DefaultVersion extends Version {
	public DefaultVersion() {
		setMajorVersion(1L);
		setMinorVersion(0L);
		setInstalledAt(new Date());
	}

	@Override
	public String toString() {
		return String.format("%d.%d", getMajorVersion(), getMinorVersion());
	}

}
