package wicket.in.action.chapter10.resdiscounts2;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import wicket.in.action.chapter10.resdiscounts.DiscountsEditList;
import wicket.in.action.chapter10.resdiscounts.DiscountsList;
import wicket.in.action.common.AdminOnly;
import wicket.in.action.common.DataBase;

public final class DiscountsPanel extends Panel {

  @AdminOnly
  private class ModeLink extends Link {

    ModeLink(String id) {
      super(id);
      IModel linkLabelModel = new AbstractReadOnlyModel() {
        @Override
        public Object getObject() {
          return inEditMode ? "[display]" : "[edit]";
        }
      };
      add(new Label("linkLabel", linkLabelModel));
    }

    @Override
    public void onClick() {
      inEditMode = !inEditMode;
      setContentPanel();
    }
  }

  private boolean inEditMode = false;

  public DiscountsPanel(String id) {

    super(id);

    add(new DiscountsList("content"));

    add(new ModeLink("modeLink"));

    final WebResource export = new WebResource() {

      @Override
      public IResourceStream getResourceStream() {
        CharSequence discounts = DataBase.getInstance()
            .exportDiscounts();
        return new StringResourceStream(discounts, "text/plain");
      }

      @Override
      protected void setHeaders(WebResponse response) {
        super.setHeaders(response);
        response.setAttachmentHeader("discounts.csv");
      }
    };
    export.setCacheable(false);

    Form form = new Form("exportForm");
    add(form);
    form.add(new SubmitLink("exportLink", new Model("export")) {

      @Override
      public void onSubmit() {
        CharSequence export = DataBase.getInstance()
            .exportDiscounts();
        ResourceStreamRequestTarget target = new ResourceStreamRequestTarget(
            new StringResourceStream(export, "text/plain"));
        target.setFileName("discounts.csv");
        RequestCycle.get().setRequestTarget(target);
      }
    });
  }

  void setContentPanel() {
    if (inEditMode) {
      addOrReplace(new DiscountsEditList("content"));
    } else {
      addOrReplace(new DiscountsList("content"));
    }
  }
}