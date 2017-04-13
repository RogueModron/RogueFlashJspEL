package app.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OffsetDateTimePersistencePGTimestampConverter implements AttributeConverter<OffsetDateTime, Timestamp>
{
	// See:
	// https://github.com/marschall/threeten-jpa

	private static ZoneId zoneIdUTC = ZoneId.of("UTC");

	@Override
	public Timestamp convertToDatabaseColumn(OffsetDateTime attribute)
	{
		if (attribute == null)
		{
			return null;
		}

		long timeMillis = attribute.atZoneSameInstant(zoneIdUTC).toInstant().getEpochSecond() * 1000;
		Timestamp timestamp = new Timestamp(timeMillis);
		timestamp.setNanos(attribute.getNano());
		return timestamp;
	}

	@Override
	public OffsetDateTime convertToEntityAttribute(Timestamp dbData)
	{
		if (dbData == null)
		{
			return null;
		}

		Instant instant = dbData.toInstant();
		OffsetDateTime odt = OffsetDateTime.ofInstant(instant, zoneIdUTC);
		return odt;
	}
}
