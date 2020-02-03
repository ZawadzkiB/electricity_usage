// https://docs.cypress.io/api/introduction/api.html

describe("App tests", () => {
  it("Load data", () => {
    cy.visit(Cypress.env("url"));
    cy.get(".v-data-table__empty-wrapper").should('be.not.visible')
    cy.get("[name='loadData']").click()
    cy.get("tbody tr").should(rows => {
      expect(rows).to.have.length(10)
    })
  });
});
