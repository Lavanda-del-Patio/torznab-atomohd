package es.lavanda.torznab.atomohd.scheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.lavanda.torznab.atomohd.service.AtomoHDService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AtomohdScheduler {

  private final AtomoHDService atomoHDServiceImpl;

  @Scheduled(fixedRate = 3600000)
  public void updateAtomohd() {
    atomoHDServiceImpl.updateInternalData();
  }

}
