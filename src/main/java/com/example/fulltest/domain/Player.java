package com.example.fulltest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank(message = "Field name cant be empty")
  private String name;

  @Min(value = 18, message = "Min age is 18")
  private Long age;
}
