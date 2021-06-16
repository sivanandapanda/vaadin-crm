package org.vaadin.crm.ui.view.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.crm.backend.entity.Company;
import org.vaadin.crm.backend.entity.Contact;
import org.vaadin.crm.backend.service.CompanyService;
import org.vaadin.crm.backend.service.ContactService;
import org.vaadin.crm.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {

    private final ContactForm form;
    private final ContactService contactService;
    private final TextField filterText = new TextField();
    private final Grid<Contact> grid = new Grid<>(Contact.class);

    public ListView(ContactService contactService, CompanyService companyService) {
        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        HorizontalLayout toolBar = getToolBar();

        form = new ContactForm(companyService.findAll());
        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(toolBar, content);
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact", click -> addContact());

        HorizontalLayout toolBar = new HorizontalLayout(filterText, addContactButton);
        toolBar.addClassName("toolbar");
        return toolBar;
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status");
        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "" : company.getName();
        }).setHeader("Company");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editContacts(event.getValue()));
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContacts(new Contact());
    }

    private void editContacts(Contact contact) {
        if(contact == null) {
             closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }

    private void saveContact(ContactForm.SaveEvent event) {
        contactService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        contactService.delete(event.getContact());
        updateList();
        closeEditor();
    }
}
