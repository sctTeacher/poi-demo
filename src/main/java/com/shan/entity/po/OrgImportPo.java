package com.shan.entity.po;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrgImportPo {

  private String orgName;
  private String appName;
  private String type ;
  private String effectiveTime;


}
