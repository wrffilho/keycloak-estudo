Context:
We are shaping a learning project for a Brazilian developer who wants to learn Keycloak Authorization Services by building a demo application.

User goals:
- Learn fine-grained authorization in Keycloak using resources, scopes, policies, permissions, and endpoint URIs.
- Build a runnable demo application rather than only reading documentation.
- Use Brazilian Portuguese names for classes, functions, endpoints, comments, examples, and docs when reasonable, because English adds cognitive load.
- Include endpoint categories that visibly demonstrate:
  1. public/free access;
  2. authenticated access;
  3. authorization-gated access through Keycloak resource/policy/permission;
  4. denied access when the user lacks permission.

Research notes:
- The repository is nearly empty, so this can define the first project shape.
- Keycloak Authorization Services models protected resources with URIs, scopes as actions, policies as conditions, and permissions as links between resources/scopes and policies.
- Policy enforcement modes matter for learning: enforcing denies by default, permissive allows when no policy applies, disabled allows all.
- The IAM market is large and growing, making this learning goal professionally relevant.

Decision dilemma:
What should the V1 scope be so the project is useful, understandable in PT-BR, technically honest about Keycloak Authorization Services, and still small enough to finish?

Other advisors in this council:
pragmatic-engineer, security-advocate, product-mind, devils-advocate.

Task:
Deliver your opening statement in 2-3 paragraphs. End with a one-line "Key Point:".
