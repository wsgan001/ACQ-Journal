set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [2.2242369346836E4	2.226934084049E4	2.217429908496E4	2.213070100721E4	 2.231229214934E4];
y2 = [1.774299817E2	1.966873959E2	2.13267076468E2	1.961229894E2	1.722651247E2];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 100.0 30000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','update', 2);
set(leg,'edgecolor','white');
set(gca,'ytick',[10^2,10^3,10^4]);
set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);