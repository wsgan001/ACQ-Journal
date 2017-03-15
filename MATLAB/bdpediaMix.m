set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [7.262833805309E4	7.689209177147E4	7.709732627715E4	7.685651768649E4	 7.668337477545E4];
y2 = [4.5647206869E2	3.2044176446E2	8.9655908763E2	1.15866245155E3	6.3447474101E2];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 100.0 100000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','update', 2);
set(leg,'edgecolor','white');
set(gca,'ytick',[10^2,10^3,10^4,10^5]);
set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);