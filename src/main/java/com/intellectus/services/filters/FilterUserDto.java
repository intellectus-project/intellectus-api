package com.intellectus.services.filters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@Builder
public class FilterUserDto {

    @Builder.Default
    private Optional<Long> id = Optional.empty();
    private String search;
    @Builder.Default
    private Optional<Boolean> enabled = Optional.empty();
    @Builder.Default
    private Optional<String> role = Optional.empty();
    @Builder.Default
    private Optional<String> country = Optional.empty();
    @Builder.Default
    private Optional<String> zone = Optional.empty();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterUserDto that = (FilterUserDto) o;
        if (that.id.isPresent() && (id == null || !id.isPresent())) return false;
        if (!that.id.isPresent() && id != null && id.isPresent()) return false;
        if (that.id.isPresent() && id.isPresent() && !that.id.get().equals(id.get())) return false;

        if (that.enabled.isPresent() && (enabled == null || !enabled.isPresent())) return false;
        if (!that.enabled.isPresent() && enabled != null && enabled.isPresent()) return false;
        if (that.enabled.isPresent() && enabled.isPresent() && !that.enabled.get().equals(enabled.get())) return false;

        if (that.role.isPresent() && (role == null || !role.isPresent())) return false;
        if (!that.role.isPresent() && role != null && role.isPresent()) return false;
        if (that.role.isPresent() && role.isPresent() && !that.role.get().equals(role.get())) return false;

        if (that.country.isPresent() && (country == null || !country.isPresent())) return false;
        if (!that.country.isPresent() && country != null && country.isPresent()) return false;
        if (that.country.isPresent() && country.isPresent() && !that.country.get().equals(country.get())) return false;

        if (that.zone.isPresent() && (zone == null || !zone.isPresent())) return false;
        if (!that.zone.isPresent() && zone != null && zone.isPresent()) return false;
        if (that.zone.isPresent() && zone.isPresent() && !that.zone.get().equals(zone.get())) return false;

        return Objects.equals(search, that.search);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, search, enabled, role, country, zone);
    }
}
