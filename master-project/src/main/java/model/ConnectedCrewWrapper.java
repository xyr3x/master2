package model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XMLRootElement(name = "connectedCrews")
public class ConnectedCrewWrapper {
	private List<ConnectedFireFighter> crew;
	
	@XmlElement(name = "fighter")
	public List<ConnectedFireFighter> getCrew(){
		return crew;
	}
	
	public void setPoints(List <OneDimPoint> crew) {
		this.crew = crew;
	}
}
