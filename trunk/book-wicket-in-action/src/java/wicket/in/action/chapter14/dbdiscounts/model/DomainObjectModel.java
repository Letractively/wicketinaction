package wicket.in.action.chapter14.dbdiscounts.model;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import wicket.in.action.chapter14.dbdiscounts.dao.GenericDao;
import wicket.in.action.chapter14.dbdiscounts.domain.DomainObject;
import wicket.in.action.common.Objects;

public class DomainObjectModel<T extends DomainObject> extends
    LoadableDetachableModel {

  @SpringBean
  private GenericDao dao;

  private final Class<T> type;

  private final Long id;

  public DomainObjectModel(Class<T> type, Long id) {
    InjectorHolder.getInjector().inject(this);
    this.type = type;
    this.id = id;
  }

  @SuppressWarnings("unchecked")
  public DomainObjectModel(T domainObject) {
    super(domainObject);
    InjectorHolder.getInjector().inject(this);
    this.type = (Class<T>) domainObject.getClass();
    this.id = domainObject.getId();
  }

  @Override
  public boolean equals(Object obj) {
    return Objects.equal(obj, getObject());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getObject());
  }

  @Override
  protected T load() {
    return dao.load(type, id);
  }
}